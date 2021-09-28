var countDate = new Date("Sep 4, 2021 12:00:00").getTime();
function timer(){
	var now = new Date().getTime();
	gap = countDate - now;
	var second = 1000;
	var minute = second * 60;
	var hour = minute * 60;
	var day = hour * 24;
	
	var d = Math.floor(gap/(day));
	var h = Math.floor((gap%day)/(hour));
	var m = Math.floor((gap%(hour))/(minute));
	var w = Math.floor((gap%(minute))/(second));
	
	document.getElementById('day').innerText = d;
	document.getElementById('hour').innerText = h;
	document.getElementById('minute').innerText = m;
	document.getElementById('second').innerText = w;
}

setInterval(function(){
	timer();},1000)
