import { useRef } from "react";
import classes from "../addForm.module.css";
import { useHistory } from "react-router-dom";
import { useState } from "react";
import { postOpenShop } from "../../api";
import visitorId from "../VisitorId";

function OpenShopForm(props) {
  const [validInput, setValid] = useState(true);
  const shopName = useRef();
  const phone = useRef();
  const creditCard = useRef();
  const shopDes = useRef();
  const shopLocation = useRef();
  const history = useHistory();

  function failInput(res) {
    alert(res.ErrorMessage);
    history.replace("/markt");
  }
  function Succes() {
    alert("The action completed succesfuly");
    history.replace("/market");
  }
  function cancelHandler() {
    history.replace('/market');
  }
  const openShop = async (data) => {
    console.log(data);

    const result = await postOpenShop(
      visitorId.id,
      data.phoneNumber,
      data.creditCard,
      data.shopName,
      data.shopDes,
      data.shopLocation
    ).then((res) => {
      if (res.ErrorMessage != null) {
        setValid(false);
      } else {
        setValid(true);
      }
      console.log(res);
      return res;
    });
    return result;
  };
  function submitHandler(event) {
    event.preventDefault();
    const enteredPhone = phone.current.value;
    const enterCreditCard = creditCard.current.value;
    const enterShopName = shopName.current.value;
    const enterShopDes = shopDes.current.value;
    const enterShopLocation = shopLocation.current.value;
    const data = {
      phoneNumber: enteredPhone,
      creditCard: enterCreditCard,
      shopName: enterShopName,
      shopDes: enterShopDes,
      shopLocation: enterShopLocation,
    };
    openShop(data);
    validInput ? Succes() : failInput();
  }

  return (
    <div>
    <form onSubmit={submitHandler}>
      <div className={classes.control}>
        <h1 className={classes.h1}>Open shop:</h1>
      </div>
      <div className={classes.control}>
        <label htmlFor="">phone number: </label>
        <input type="text" required id="phone" ref={phone} size="20" />
      </div>
      <div className={classes.control}>
        <label htmlFor="">credit card: </label>
        <input
          type="text"
          required
          id="credit card"
          ref={creditCard}
          size="20"
        />
      </div>
      <div className={classes.control}>
        <label htmlFor="">enter shop name: </label>
        <input type="text" required id="shop name" ref={shopName} size="20" />
      </div>
      <div className={classes.control}>
        <label htmlFor="">write shop description: </label>
        <input
          type="text"
          required
          id="shop destination"
          ref={shopDes}
          size="20"
        />
      </div>
      <div className={classes.control}>
        <label htmlFor="">enter shop location: </label>
        <input
          type="text"
          required
          id="shop location"
          ref={shopLocation}
          size="20"
        />
      </div>
      <div>
        <button className={classes.actions}>open shop</button>
      </div>
    </form>
       <button className={classes.returnButton} onClick={cancelHandler}>
       Return
     </button>
     </div>
 
  );
}
export default OpenShopForm;
