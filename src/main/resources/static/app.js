var stompClient = null;
var clientId = null;

reset();

function reset(){

    $("#connect").hide();
    $("#disconnect").show();
    stompClient = null;
    clientId = null;
    $("#greetings").empty();
}

function startChatServer(){

    var id = $("#clientId").val();
    if(id !=null && id.length>0){
        clientId = id;
        connect();
    }else{
        alert("Id should be min of 1 Character");
    }
}

function setConnected(connected) {

    if(connected){
        $("#connect").show();
        $("#disconnect").hide();
    }else{
        $("#connect").hide();
        $("#disconnect").show();
    }
}

function connect() {
    if(clientId==null){
        alert("client Id cannot be null.Please set your id");
    }
    var socket = new SockJS('/input');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        var subScribeTo = '/topic/'+clientId;
        stompClient.subscribe(subScribeTo, function (greeting) {

            console.log("Got Data");
            console.log(JSON.parse(greeting.body));
            console.log("Showing Got Data");
            showGreeting(JSON.parse(greeting.body));
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {

    var msg = $("#messageBox").val();
    var des = $("#messageBoxDes").val();


    stompClient.send("/app/message", {}, JSON.stringify({'sourceClientId':clientId,'destinationClientId':des,
            'message':msg}));
    $("#messageBox").val("");
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message.destinationClientId + "</td>"+"<td>" + message.message + "</td></tr>");
}

$(function () {
    $("#startServerForm").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { startChatServer(); });

    $("#sendMessage").click(function(){sendMessage();});
});
