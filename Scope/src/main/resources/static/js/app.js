
/* SLIDER PRODUCTOS */
var container = document.querySelector('.slider'),
    btnLeft = document.getElementById("btn-left"),
    btnRight = document.getElementById("btn-right"),
    slider = document.getElementById("slider"),
    index = 0;

document.addEventListener('touchstart', handleTouchStart, false);
document.addEventListener('touchmove', handleTouchMove, false);

var xDown = null;
var yDown = null;

function getTouches(evt) {
    return evt.touches || // browser API
        evt.originalEvent.touches; // jQuery
}

function handleTouchStart(evt) {
    const firstTouch = getTouches(evt)[0];
    xDown = firstTouch.clientX;
    yDown = firstTouch.clientY;
};

function handleTouchMove(evt) {
    if (!xDown || !yDown) {
        return;
    }

    var xUp = evt.touches[0].clientX;
    var yUp = evt.touches[0].clientY;

    var xDiff = xDown - xUp;
    var yDiff = yDown - yUp;

    if (Math.abs(xDiff) > Math.abs(yDiff)) { /*most significant*/
        if (xDiff > 0) {
            if (index != 4) {
                container.scrollLeft += container.offsetWidth ;
                index++;
            } else {
                container.scrollLeft -= container.offsetWidth * 4;
                index = 0;
            }
        } else {
            if (index != 0) {
                container.scrollLeft -= container.offsetWidth;
                index--;
            } else {
                container.scrollLeft += container.offsetWidth * 4;
                index = 4;
            }
        }
    } else {
        if (yDiff > 0) {
            /* down swipe */
        } else {
            /* up swipe */
        }
    }
    /* reset values */
    xDown = null;
    yDown = null;
};

