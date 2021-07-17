var editorA;

$(function () {
  editorA = null;
  //富文本编辑器 用于商品详情编辑
  const F = window.wangEditor;
  editorA = new F('#wangEditor')
  // 设置编辑区域高度为 750px
  editorA.config.height = 750
  //配置服务端图片上传地址
  editorA.config.uploadImgServer = '/admin/upload/files'
  editorA.config.uploadFileName = 'files'
  //限制图片大小 2M
  editorA.config.uploadImgMaxSize = 2 * 1024 * 1024
  //限制一次最多能传几张图片 一次最多上传 5 个图片
  editorA.config.uploadImgMaxLength = 5
  //隐藏插入网络图片的功能
  editorA.config.showLinkImg = false
  editorA.config.uploadImgHooks = {
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
  editorA.create();

  //图片上传插件初始化 用于商品预览图上传
  new AjaxUpload('#uploadgroupCoverImg', {
    action: '/admin/upload/file',
    name: 'file',
    autoSubmit: true,
    responseType: "json",
    onSubmit: function (file, extension) {
      if (!(extension && /^(jpg|jpeg|png|gif)$/.test(extension.toLowerCase()))) {
        alert('只支持jpg、png、gif格式的文件！');
        return false;
      }
    },
    onComplete: function (file, r) {
      if (r != null && r.resultCode == 200) {
        $("#groupCoverImg").attr("src", r.data);
        $("#groupCoverImg").attr("style", "width: 128px;height: 128px;display:block;");
        return false;
      } else {
        alert("error");
      }
    }
  });
});

$('#saveButton').click(function () {
  var groupId = $('#groupId').val();
  var groupCategoryId = $('#levelThree option:selected').val();
  var groupName = $('#groupName').val();
  var tag = $('#tag').val();
  var originalPrice = $('#originalPrice').val();
  var groupIntro = $('#groupIntro').val();
  var stockNum = $('#stockNum').val();
  var groupSellStatus = $("input[name='groupSellStatus']:checked").val();
  var groupDetailContent = editorA.txt.html();
  var groupCoverImg = $('#groupCoverImg')[0].src;
  var groupNum = $('#groupNum').val();
  var groupPrice = $('#groupPrice').val();

  if (isNull(groupCategoryId)) {
    swal("请选择分类", {
      icon: "error",
    });
    return;
  }
  if (isNull(groupName)) {
    swal("请输入商品名称", {
      icon: "error",
    });
    return;
  }
  if (!validLength(groupName, 100)) {
    swal("商品名称过长", {
      icon: "error",
    });
    return;
  }
  if (isNull(tag)) {
    swal("请输入商品小标签", {
      icon: "error",
    });
    return;
  }
  if (!validLength(tag, 100)) {
    swal("标签过长", {
      icon: "error",
    });
    return;
  }
  if (isNull(groupIntro)) {
    swal("请输入商品简介", {
      icon: "error",
    });
    return;
  }
  if (!validLength(groupIntro, 100)) {
    swal("简介过长", {
      icon: "error",
    });
    return;
  }
  if (isNull(originalPrice) || originalPrice < 1) {
    swal("请输入商品价格", {
      icon: "error",
    });
    return;
  }
  if (isNull(stockNum) || stockNum < 0) {
    swal("请输入商品库存数", {
      icon: "error",
    });
    return;
  }
  if (isNull(groupSellStatus)) {
    swal("请选择上架状态", {
      icon: "error",
    });
    return;
  }
  if (isNull(groupDetailContent)) {
    swal("请输入商品介绍", {
      icon: "error",
    });
    return;
  }
  // if (isNull(groupPrice)) {
  //   swal("请输入拼团价格", {
  //     icon: "error",
  //   });
  //   return;
  // }
  if (isNull(groupNum)) {
    swal("请输入成团人数", {
      icon: "error",
    });
    return;
  }
  if (!validLength(groupDetailContent, 50000)) {
    swal("商品介绍内容过长", {
      icon: "error",
    });
    return;
  }
  if (isNull(groupCoverImg) || groupCoverImg.indexOf('img-upload') != -1) {
    swal("封面图片不能为空", {
      icon: "error",
    });
    return;
  }
  var url = '/admin/group/save';
  var swlMessage = '保存成功';
  var data = {
    "groupName": groupName,
    "groupIntro": groupIntro,
    "groupCategoryId": groupCategoryId,
    "tag": tag,
    "originalPrice": originalPrice,
    "stockNum": stockNum,
    "groupDetailContent": groupDetailContent,
    "groupCoverImg": groupCoverImg,
    "groupSellStatus": groupSellStatus,
    "groupPrice": groupPrice,
    "groupNum": groupNum
  };
  if (groupId > 0) {
    url = '/admin/group/update';
    swlMessage = '修改成功';
    data = {
      "groupId": groupId,
      "groupName": groupName,
      "groupIntro": groupIntro,
      "groupCategoryId": groupCategoryId,
      "tag": tag,
      "originalPrice": originalPrice,
      "stockNum": stockNum,
      "groupDetailContent": groupDetailContent,
      "groupCoverImg": groupCoverImg,
      "groupSellStatus": groupSellStatus,
      "groupPrice": groupPrice,
      "groupNum": groupNum
    };
  }
  console.log(data);
  $.ajax({
    type: 'POST',//方法类型
    url: url,
    contentType: 'application/json',
    data: JSON.stringify(data),
    success: function (result) {
      console.log(result);
      if (result.resultCode == 200) {
        $('#groupModal').modal('hide');
        swal({
          title: swlMessage,
          type: 'success',
          showCancelButton: false,
          confirmButtonColor: '#1baeae',
          confirmButtonText: '返回拼团商品列表',
          confirmButtonClass: 'btn btn-success',
          buttonsStyling: false
        }).then(function () {
          window.location.href = "/admin/group";
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
  window.location.href = "/admin/group";
});

$('#levelOne').on('change', function () {
  $.ajax({
    url: '/admin/categories/listForSelect?categoryId=' + $(this).val(),
    type: 'GET',
    success: function (result) {
      if (result.resultCode == 200) {
        var levelTwoSelect = '';
        var secondLevelCategories = result.data.secondLevelCategories;
        var length2 = secondLevelCategories.length;
        for (var i = 0; i < length2; i++) {
          levelTwoSelect += '<option value=\"' + secondLevelCategories[i].categoryId + '\">' + secondLevelCategories[i].categoryName + '</option>';
        }
        $('#levelTwo').html(levelTwoSelect);
        var levelThreeSelect = '';
        var thirdLevelCategories = result.data.thirdLevelCategories;
        var length3 = thirdLevelCategories.length;
        for (var i = 0; i < length3; i++) {
          levelThreeSelect += '<option value=\"' + thirdLevelCategories[i].categoryId + '\">' + thirdLevelCategories[i].categoryName + '</option>';
        }
        $('#levelThree').html(levelThreeSelect);
      } else {
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

$('#levelTwo').on('change', function () {
  $.ajax({
    url: '/admin/categories/listForSelect?categoryId=' + $(this).val(),
    type: 'GET',
    success: function (result) {
      if (result.resultCode == 200) {
        var levelThreeSelect = '';
        var thirdLevelCategories = result.data.thirdLevelCategories;
        var length = thirdLevelCategories.length;
        for (var i = 0; i < length; i++) {
          levelThreeSelect += '<option value=\"' + thirdLevelCategories[i].categoryId + '\">' + thirdLevelCategories[i].categoryName + '</option>';
        }
        $('#levelThree').html(levelThreeSelect);
      } else {
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



