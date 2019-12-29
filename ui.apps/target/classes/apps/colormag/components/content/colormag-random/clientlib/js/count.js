// Stop carousel



setTimeout(function(){

var li_elements = document.getElementsByClassName("indica");
for (var i = 0 ; i < li_elements.length; i++ ) {
    li_elements[ i ].setAttribute("data-slide-to", i);
}
}, 1000);