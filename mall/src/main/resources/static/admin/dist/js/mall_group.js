$(function () {
  $("#jqGrid").jqGrid({
    url: '/admin/group/list',
    datatype: "json",
    colModel: [
      {label: '商品编号', name: 'groupId', index: 'groupId', width: 60, key: true},
      {label: '商品名', name: 'groupName', index: 'groupName', width: 120},
      {label: '商品简介', name: 'groupIntro', index: 'groupIntro', width: 120},
      {label: '商品图片', name: 'groupCoverImg', index: 'groupCoverImg', width: 120, formatter: coverImageFormatter},
      {label: '商品库存', name: 'stockNum', index: 'stockNum', width: 60},
      {label: '商品售价', name: 'originalPrice', index: 'originalPrice', width: 60},
      {label: '拼团价', name: 'groupPrice', index: 'groupPrice', width: 60},
      {label: '成团数量', name: 'groupNum', index: 'groupNum', width: 60},
      {
        label: '上架状态',
        name: 'groupSellStatus',
        index: 'groupSellStatus',
        width: 80,
        formatter: groupSellStatusFormatter
      }
    ],
    height: 760,
    rowNum: 20,
    rowList: [20, 50, 80],
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
      order: "order",
    },
    gridComplete: function () {
      //隐藏grid底部滚动条
      $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
    }
  });

  $(window).resize(function () {
    $("#jqGrid").setGridWidth($(".card-body").width());
  });

  function groupSellStatusFormatter(cellvalue) {
    //商品上架状态 0-上架 1-下架
    if (cellvalue == 0) {
      return "<button type=\"button\" class=\"btn btn-block btn-success btn-sm\" style=\"width: 80%;\">销售中</button>";
    }
    if (cellvalue == 1) {
      return "<button type=\"button\" class=\"btn btn-block btn-secondary btn-sm\" style=\"width: 80%;\">已下架</button>";
    }
  }

  function coverImageFormatter(cellvalue) {
    return "<img src='" + cellvalue + "' height=\"80\" width=\"80\" alt='商品主图'/>";
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


/**
 * 添加商品
 */
function addGroup() {
  window.location.href = "/admin/group/edit";
}

/**
 * 修改商品
 */
function editGroup() {
  var id = getSelectedRow();
  if (id == null) {
    return;
  }
  window.location.href = "/admin/group/edit/" + id;
}

/**
 * 上架
 */
function putUpGroup() {
  var ids = getSelectedRows();
  if (ids == null) {
    return;
  }
  swal({
    title: "确认弹框",
    text: "确认要执行上架操作吗?",
    icon: "warning",
    buttons: true,
    dangerMode: true,
  }).then((flag) => {
      if (flag) {
        $.ajax({
          type: "PUT",
          url: "/admin/group/status/0",
          contentType: "application/json",
          data: JSON.stringify(ids),
          success: function (r) {
            if (r.resultCode == 200) {
              swal("上架成功", {
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
function putDownGroup() {
  var ids = getSelectedRows();
  if (ids == null) {
    return;
  }
  swal({
    title: "确认弹框",
    text: "确认要执行下架操作吗?",
    icon: "warning",
    buttons: true,
    dangerMode: true,
  }).then((flag) => {
      if (flag) {
        $.ajax({
          type: "PUT",
          url: "/admin/group/status/1",
          contentType: "application/json",
          data: JSON.stringify(ids),
          success: function (r) {
            if (r.resultCode == 200) {
              swal("下架成功", {
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





