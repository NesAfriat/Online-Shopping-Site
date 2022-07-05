import {useRef} from "react";
import classes from "../../../../styling/addForm.module.css";
import {postAddTotalShopDiscount} from "../../../../api";

const AddTotalShopDiscount = (props) => {
    const percentageRef = useRef();
    const expireYearRef = useRef();
    const expireMonthRef = useRef();
    const expireDayRef = useRef();

    const submitHandler = async (e) => {
        e.preventDefault();
        const enteredPercentage = percentageRef.current.value / 100;
        const enteredExpireYear = expireYearRef.current.value;
        const enteredExpireMonth = expireMonthRef.current.value;
        const enteredExpireDay = expireDayRef.current.value;
        await postAddTotalShopDiscount(
            props.visitorId,
            props.shopId,
            enteredPercentage,
            enteredExpireYear,
            enteredExpireMonth,
            enteredExpireDay,
        ).then((res) => {
            console.log(res);
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage)
            } else {
                alert("Successfully added discount");
                props.setter(false);
            }
        });
    };

    return (
        <div>
            <form onSubmit={submitHandler}>
                <div className={classes.control}>
                    <h1 className={classes.h1}>Enter discount information:</h1>
                    <label htmlFor="percentage">Percentage: </label>
                    <input type="number" required id="percentage" ref={percentageRef}/>
                </div>
                <div className={classes.control}>
                    <label htmlFor="expireYear">Expiration Year: </label>
                    <input type="number" required id="expireYear" ref={expireYearRef}/>
                </div>
                <div className={classes.control}>
                    <label htmlFor="expireMonth">Expiration Month: </label>
                    <input type="number" required id="expireMonth" ref={expireMonthRef}/>
                </div>
                <div className={classes.control}>
                    <label htmlFor="expireDay">Expiration Day: </label>
                    <input type="number" required id="expireDay" ref={expireDayRef}/>
                </div>
                <div>
                    <button className={classes.actions}>Add new discount</button>
                </div>
            </form>
        </div>
    );
};

export default AddTotalShopDiscount;
