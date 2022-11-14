/**
 * Created by alfred on 07 October 2020
 */
$(document).ready(function () {
    $(".bid-timer").each(function () {
        let timer = $(this);
        let expiryTimeString = timer.data("remaining-time");
        let expiryTime = moment(expiryTimeString, 'Do/MM/YYYY hh:mm:ss a');

        timer.countdown({until: expiryTime.toDate(), compact: true,
            layout: '<b>{dn}{dl} {hnn}{sep}{mnn}{sep}{snn}</b> {desc}',
            description: 'to expiry'});
    });
});