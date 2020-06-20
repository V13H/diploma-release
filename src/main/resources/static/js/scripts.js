$(document).ready(function () {
    $('.sidenav').sidenav();
});
$('.dropdown-trigger').dropdown({
    coverTrigger: false,
    constrainWidth: false
});
$(document).ready(function () {
    $('.fixed-action-btn').floatingActionButton();
});
$(document).ready(function () {
    $('.sidenav').sidenav();
});
$(document).ready(function () {
    $('#main-sidenav').sidenav();
});

$(document).ready(function () {
    $('.collapsible').collapsible();
});

$(document).ready(function () {
    var currentDate = new Date();
    var month = currentDate.getMonth();
    var day = currentDate.getDate();
    var year = currentDate.getFullYear();
    $('.datepicker').datepicker({
        minDate: new Date(year, month, day),
        defaultDate: new Date(year, month, day),
        format: 'dd.mm.yyyy'
    });
});
$(document).ready(function () {
    $('.modal').modal()
});
$(document).ready(function () {
    $('#greetingsModal').modal({
        dismissible: false
    });
    $('#greetingsModal').modal('open')
});
$(document).ready(function () {
    $('.timepicker').timepicker({
        twelveHour: false
    });
});
$(document).ready(function () {
    $('.tabs').tabs();
});

$(document).ready(function () {
    $('.collapsible').collapsible();
});




