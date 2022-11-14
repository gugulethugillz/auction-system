$('document').ready(function() {
    $('.table #detailsButton').on('click',function(event) {

        event.preventDefault();
        var href= $(this).attr('href');
        $.get(href, function(payment){
            $('#idDetails').val(payment.id);
            $('#amount').val(payment.value);
            $('#assetName').val(payment.asset);
            $('#payMethod').val(payment.paymentMethod);
            $('#bid').val(payment.bid);
            $('#paidBy').val(payment.user);
            $('#date').val(payment.date);
        });
        $('#detailsModal').modal();
    });
});