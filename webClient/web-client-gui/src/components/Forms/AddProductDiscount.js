import { useRef } from "react";
import classes from "../addForm.module.css";
import { useHistory } from "react-router-dom";
import { useState } from "react";
import { postAddProductDiscount } from "../../api";
import visitorId from "../VisitorId";

function AddProductDiscountForm(props) {
  const [validInput, setValid] = useState(true);
  const history = useHistory();
  const percentageRef = useRef();
  const expireYearRef = useRef();
  const expireMonthRef = useRef();
  const expireDayRef = useRef();
  const productIdRef = useRef();

  function failInput(errorMessage) {
    setValid(false);
    alert(errorMessage);
    history.replace(`/all-shops/${props.shopId}/create-new-product-discount`);
  }
  function Succes() {
    setValid(true);
    alert("The action completed succesfuly");
    history.replace(`/all-shops/${props.shopId}`);
  }
  function cancelHandler() {
    history.replace(`/all-shops/${props.shopId}`);
  }
  const CreateNewProductDiscount = async (productDiscountData) => {
    const result = await postAddProductDiscount(
      visitorId.id,
      props.shopId,
      productDiscountData.percentage,
      productDiscountData.expireYear,
      productDiscountData.expireMonth,
      productDiscountData.expireDay,
      productDiscountData.productId
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
    const enteredPercentage = percentageRef.current.value;
    const enteredExpireYear = expireYearRef.current.value;
    const enteredExpireMonth = expireMonthRef.current.value;
    const enteredExpireDay = expireDayRef.current.value;
    const enteredProductId = productIdRef.current.value;

    const productDiscountData = {
      percentage: enteredPercentage,
      expireYear: enteredExpireYear,
      expireMonth: enteredExpireMonth,
      expireDay: enteredExpireDay,
      productId: enteredProductId,
    };

    const res = CreateNewProductDiscount(productDiscountData);
    validInput ? Succes() : failInput(res.errorMessage);
  }
  return (
    <div>
    <form onSubmit={submitHandler}>
      <div className={classes.control}>
        <h1 className={classes.h1}>enter product discount infornation:</h1>
        <label htmlFor="percentage">percentage: </label>
        <input type="number" required id="percentage" ref={percentageRef} />
      </div>
      <div className={classes.control}>
        <label htmlFor="expireYear">expireYear: </label>
        <input type="number" required id="expireYear" ref={expireYearRef} />
      </div>
      <div className={classes.control}>
        <label htmlFor="expireMonth">expireMonth: </label>
        <input type="number" required id="expireMonth" ref={expireMonthRef} />
      </div>
      <div className={classes.control}>
        <label htmlFor="expireDay">expireDay: </label>
        <input type="number" required id="expireDay" ref={expireDayRef} />
      </div>
      <div className={classes.control}>
        <label htmlFor="productId">productId: </label>
        <input type="number" required id="productId" ref={productIdRef} />
      </div>
      <div>
        <button className={classes.actions}>Sign up</button>
      </div>
    </form>
         <button className={classes.returnButton} onClick={cancelHandler}>
         Return
       </button>
       </div>
  );
}

export default AddProductDiscountForm;
