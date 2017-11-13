$(document).ready(function(){
  $response = $('#responseField');

  function predict() {
    var data = {};

    $('input').each(function(){
      var $field = $(this);
      data[$field.attr('data-column')] = $field.val();
    });

    $('select').each(function(){
      var $field = $(this);
      data[$field.attr('data-column')] = $field.val();
    });

    var xhr = $.getJSON('http://localhost:8080/Titanic_Webapp/predict', data)
      .done(function(data){
        console.log(data);
        console.log("test");
        if(data.Survived === 1) {
          $response.text('Congratulation! You Survived!');
        } else if (data.Survived === 0) {
          $response.text('Sorry, you are dead :(');
        }
      })
      .fail(function(error){
        console.error(error.responseText ? error.responseText : error);
        $response.text('');
      })
  }

  var updatePrediction = _.debounce(predict, 250);

  $('input').each(function(){
    $(this).keydown(updatePrediction);
  });

  $('select').each(function(){
    $(this).change(updatePrediction);
  });

  $("form").bind("keypress", function (e) {
    if (e.keyCode == 13) {
      return false;
    }
  });

  updatePrediction();

});