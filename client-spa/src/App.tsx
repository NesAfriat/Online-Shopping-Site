import "./App.css";
import { useState } from "react";
import { postVisit } from "./api";
import Market from "./components/Market.jsx";

const App = () => {
  const [visitorId, setVisitorId] = useState(-1);

  const visitHandler = async () => {
    console.log(`clicked on visit`);
    await postVisit().then((res) => {
      console.log(res)
      if(res.errorOccurred){
        alert(res.errorMessage)
      }else{
        setVisitorId(res.value.id);
      }

    });
  };

  return (
    <div className="App">
      {visitorId === -1 ? (
        <>
          <button onClick={visitHandler}>visit market</button>
        </>
      ) : (
        <>
          <Market visitorId={visitorId} />
        </>
      )}
    </div>
  );
};

export default App;
