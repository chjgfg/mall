$(function () {
  $("#jqGrid").jqGrid({
    url: '/admin/notice/list',
    datatype: "json",
    colModel: [
      {label: '公告编号', name: 'noticeId', index: 'noticeId', width: 60, key: true},
      {label: '公告标题', name: 'title', index: 'title', width: 120},
      {label: '创建时间', name: 'createTime', index: 'createTime', width: 120},
      {label: '修改时间', name: 'updateTime', index: 'updateTime', width: 120},
      {
        label: '发布状态',
        name: 'status',
        index: 'status',
        width: 80,
        formatter: noticeSellStatusFormatter
      }
    ],
    height: 760,
    rowNum: 10,
    rowList: [10, 20, 30],
    styleUI: 'Bootstrap',
    loadtext: '信息读取中...',
    rownumbers: false,
    rownumWidth: 20,
    autowidth: true,
    multiselect: true,
    pager: "#jqGridPager",
    jsonReader: {
      root: "data.list",
      page: "data.currPage",
      total: "data.totalPage",
      records: "data.totalCount"
    },
    prmNames: {
      page: "page",
      rows: "limit",
      order: "order"
    },
    gridComplete: function () {
      //隐藏grid底部滚动条
      $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
    }
  });


  $(window).resize(function () {
    $("#jqGrid").setGridWidth($(".card-body").width());
  });

  function noticeSellStatusFormatter(cellvalue) {
    //商品上架状态 0-上架 1-下架
    if (cellvalue == 1) {
      return "<button type=\"button\" class=\"btn btn-block btn-success btn-sm\" style=\"width: 80%;\">已发布</button>";
    }
    if (cellvalue == 0) {
      return "<button type=\"button\" class=\"btn btn-block btn-secondary btn-sm\" style=\"width: 80%;\">未发布</button>";
    }
  }

});

/**
 * jqGrid重新加载
 */
function reload() {
  initFlatPickr();
  var page = $("#jqGrid").jqGrid('getGridParam', 'page');
  $("#jqGrid").jqGrid('setGridParam', {
    page: page
  }).trigger("reloadGrid");
}

/**
 * 添加公告
 */
function addNotice() {
  window.location.href = "/admin/notice/edit";
}

/**
 * 修改公告
 */
function editNotice() {
  var id = getSelectedRow();
  if (id == null) {
    return;
  }
  window.location.href = "/admin/notice/edit/" + id;
}

/**
 * 上架
 */
function putUpNotice() {
  var ids = getSelectedRows();
  if (ids == null) {
    return;
  }
  swal({
    title: "确认弹框",
    text: "确认要执行发布操作吗?",
    icon: "warning",
    buttons: true,
    dangerMode: true,
  }).then((flag) => {
      if (flag) {
        $.ajax({
          type: "PUT",
          url: "/admin/notice/status/1",
          contentType: "application/json",
          data: JSON.stringify(ids),
          success: function (r) {
            if (r.resultCode == 200) {
              swal("发布成功", {
                icon: "success",
              });
              $("#jqGrid").trigger("reloadGrid");
            } else {
              swal(r.message, {
                icon: "error",
              });
            }
          }
        });
      }
    }
  )
  ;
}


/**
 * 下架
 */
function putDownNotice() {
  var ids = getSelectedRows();
  if (ids == null) {
    return;
  }
  swal({
    title: "确认弹框",
    text: "确认要执行撤销操作吗?",
    icon: "warning",
    buttons: true,
    dangerMode: true,
  }).then((flag) => {
      if (flag) {
        $.ajax({
          type: "PUT",
          url: "/admin/notice/status/0",
          contentType: "application/json",
          data: JSON.stringify(ids),
          success: function (r) {
            if (r.resultCode == 200) {
              swal("撤销成功", {
                icon: "success",
              });
              $("#jqGrid").trigger("reloadGrid");
            } else {
              swal(r.message, {
                icon: "error",
              });
            }
          }
        });
      }
    }
  )
  ;
}

function gradeChange() {
  var data = {
    "status": $("#levelOne").val()
  };
  $("#jqGrid_ds").jqGrid('clearGridData');
  var page = $("#jqGrid").jqGrid('getGridParam', 'page');
  $("#jqGrid").jqGrid('setGridParam', {
    postData: data,
    page: page
  }).trigger("reloadGrid");
}
