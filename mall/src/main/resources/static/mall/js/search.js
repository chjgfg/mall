$(function () {
  $('#keyword').keypress(function (e) {
    const key = e.which; //e.which是按键的值
    if (key === 13) {
      const q = $(this).val();
      if (q && q !== '') {
        window.location.href = '/search?keyword=' + q;
      }
    }
  });
});

function search() {
  const q = $('#keyword').val();
  if (q && q !== '') {
    window.location.href = '/search?keyword=' + q;
  }
}