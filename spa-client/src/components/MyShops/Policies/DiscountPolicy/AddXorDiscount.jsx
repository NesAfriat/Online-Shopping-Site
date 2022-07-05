import {useRef} from "react";
import classes from "../../../../styling/addForm.module.css";
import {postAddXorDiscount} from "../../../../api";

const AddXORDiscount = (props) => {
    const firstDiscountRef = useRef();
    const secondDiscountRef = useRef();
    const expireYearRef = useRef();
    const expireMonthRef = useRef();
    const expireDayRef = useRef();

    const submitHandler = async (e) => {
        e.preventDefault();
        const enteredFirstDiscount = firstDiscountRef.current.value;
        const enteredSecondDiscount = secondDiscountRef.current.value;
        const enteredExpireYear = expireYearRef.current.value;
        const enteredExpireMonth = expireMonthRef.current.value;
        const enteredExpireDay = expireDayRef.current.value;
        await postAddXorDiscount(
            props.visitorId,
            props.shopId,
            enteredFirstDiscount,
            enteredSecondDiscount,
            enteredExpireYear,
            enteredExpireMonth,
            enteredExpireDay
        ).then((res) => {
            console.log(res);
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage)
            } else {
                alert("Succesfully added discount");
                props.setter(false);
            }
        });
    };

    return (
        <div>
            <form onSubmit={submitHandler}>
                <div className={classes.control}>
                    <h1 className={classes.h1}>Enter discount information:</h1>
                    <div>
                        <label htmlFor="first">First discount ID: </label>
                        <input type="number" required id="first" ref={firstDiscountRef}/>
                    </div>
                    <div>
                        <label htmlFor="second">Second discount ID: </label>
                        <input type="number" required id="second" ref={secondDiscountRef}/>
                    </div>
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

export default AddXORDiscount;
