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
    $('.modal').modal();
});
$(document).ready(function () {
    var currentDate = new Date();
    var month = currentDate.getMonth();
    var day = currentDate.getDate();
    var year = currentDate.getFullYear();
    $('.datepicker').datepicker({
        minDate: new Date(year, month, day),
        defaultDate: new Date(year, month, day)
    });
});
