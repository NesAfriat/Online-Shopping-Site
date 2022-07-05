import SockJS from "sockjs-client";
import Stomp from "stompjs";

const socket = new SockJS("https://localhost:8080/greeting");
const ws = Stomp.over(socket);

export const connect = () => {
  ws.connect(
    {},
    () => {
      ws.subscribe("/user/queue/errors", function (message) {
        alert("Error " + message.body);
      });

      ws.subscribe("/user/queue/{visitor_id}/reply", function (message) {
        showGreeting(message.body);
      });
    },
    (error) => {
      alert("STOMP error " + error);
    }
  );
};

export const disconnect = () => {
  if (ws != null) {
    ws.close();
  }
  console.log("Disconnected");
};

export const sendName = () => {
  let data = JSON.stringify({
    name: "idan",
  });
  ws.send("/app/message", {}, data);
};

export const showGreeting = (message) => {
  "greetings".append(" " + message + "");
};
