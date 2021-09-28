var stompClient=null;
var table = document.getElementById("message-container-table");
var room;
var roomVar;
var clientsInRoom = 0;

function putRoom(roome){
	room = roome
	roomVar = "/app/message/"+room
	console.log(roome)
}

   function sendMessage(){
	 var var1 = document.getElementById("message-value");
	 
    let jsonOb={
        name:$("#name-value").val(),
        content:$("#message-value").val()
    }
	var1.value='';
    stompClient.send("/app/message/"+room,{},JSON.stringify(jsonOb));

   }


function connect()
{
        let socket=new SockJS("/server1")

        stompClient=Stomp.over(socket)
		clientsInRoom = stompClient.size;
		document.getElementById("viewers").value = clientsInRoom;
        stompClient.connect({},function(frame){

            console.log("Connected : "+frame)
				
            $("#name-from").addClass('d-none')
              $("#chat-room").removeClass('d-none')
                //subscribe
                stompClient.subscribe("/topic/return-to/"+room,function(response){

                        showMessage(JSON.parse(response.body))

                })
				
        })

}


 function showMessage(message)
 {
    $("#message-container-table").append(`<tr><td><b>${message.name} :</b> ${message.content}</td></tr>`)
	var rowpos = $('#message-container-table tr:last').position();
	$('#scrollTable').scrollTop(rowpos.top);
	
 }




$(document).ready((e)=>{
       connect();


   $("#send-btn").click(()=>{
    sendMessage()
   })

$("#logout").click(()=>{

    localStorage.removeItem("name")
    if(stompClient!==null)
    {
        stompClient.disconnect()

         $("#name-from").removeClass('d-none')
         $("#chat-room").addClass('d-none')
         console.log(stompClient)
    }

})

})


