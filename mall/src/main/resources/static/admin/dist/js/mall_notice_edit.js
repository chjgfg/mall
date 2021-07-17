var editorB;

$(function () {
  editorB = null;
  //富文本编辑器 用于商品详情编辑
  const G = window.wangEditor;
  editorB = new G('#wangEditor')
  // 设置编辑区域高度为 750px
  editorB.config.height = 750
  //配置服务端图片上传地址
  editorB.config.uploadImgServer = '/admin/upload/files'
  editorB.config.uploadFileName = 'files'
  //限制图片大小 2M
  editorB.config.uploadImgMaxSize = 2 * 1024 * 1024
  //限制一次最多能传几张图片 一次最多上传 5 个图片
  editorB.config.uploadImgMaxLength = 5
  //隐藏插入网络图片的功能
  editorB.config.showLinkImg = false
  editorB.config.uploadImgHooks = {
    // 图片上传并返回了结果，图片插入已成功
    success: function (xhr) {
      console.log('success', xhr)
    },
    // 图片上传并返回了结果，但图片插入时出错了
    fail: function (xhr, editor, resData) {
      console.log('fail', resData)
    },
    // 上传图片出错，一般为 http 请求的错误
    error: function (xhr, editor, resData) {
      console.log('error', xhr, resData)
    },
    // 上传图片超时
    timeout: function (xhr) {
      console.log('timeout')
    },
    customInsert: function (insertImgFn, result) {
      if (result != null && result.resultCode == 200) {
        // insertImgFn 可把图片插入到编辑器，传入图片 src ，执行函数即可
        result.data.forEach(img => {
          insertImgFn(img)
        });
      } else {
        alert("error");
      }
    }
  }
  editorB.create();

});

$('#saveButton').click(function () {

  var noticeId = $('#noticeId').val();
  var nntitle = $('#nntitle').val();
  var status = $("input[name='status']:checked").val();
  var context = editorB.txt.html();
  if (isNull(nntitle)) {
    swal("请编写题目", {
      icon: "error",
    });
    return;
  }
  if (isNull(status)) {
    swal("请选择状态", {
      icon: "error",
    });
    return;
  }


  if (isNull(context)) {
    swal("请输入公告正文", {
      icon: "error",
    });
    return;
  }

  var url = '/admin/notice/save';
  var swlMessage = '保存成功';
  var data = {
    "title": nntitle,
    "status": status,
    "context": context
  };
  if (noticeId > 0) {
    url = '/admin/notice/update';
    swlMessage = '修改成功';
    data = {
      "noticeId": noticeId,
      "title": nntitle,
      "status": status,
      "context": context,
    };
  }
  console.log(data);
  $.ajax({
    type: 'POST',//方法类型
    url: url,
    contentType: 'application/json',
    data: JSON.stringify(data),
    success: function (result) {
      if (result.resultCode == 200) {
        $('#groupModal').modal('hide');
        swal({
          title: swlMessage,
          type: 'success',
          showCancelButton: false,
          confirmButtonColor: '#1baeae',
          confirmButtonText: '返回公告列表',
          confirmButtonClass: 'btn btn-success',
          buttonsStyling: false
        }).then(function () {
          window.location.href = "/admin/notice";
        })
      } else {
        $('#groupModal').modal('hide');
        swal(result.message, {
          icon: "error",
        });
      }
      ;
    },
    error: function () {
      swal("操作失败", {
        icon: "error",
      });
    }
  });
});

$('#cancelButton').click(function () {
  window.location.href = "/admin/notice";
});




