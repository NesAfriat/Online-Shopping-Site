import {useRef} from "react";
import classes from "../../../../styling/addForm.module.css";
import {postAddAtLeastFromProductPolicy} from "../../../../api";

const AddAtLeast = (props) => {
    const productIDRef = useRef();
    const minRef = useRef();

    const submitHandler = async (e) => {
        e.preventDefault();
        const enteredProductID = productIDRef.current.value;
        const enteredMin = minRef.current.value;

        await postAddAtLeastFromProductPolicy(
            props.visitorId,
            props.shopId,
            enteredProductID,
            enteredMin
        ).then((res) => {
            console.log(res);
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage)
            } else {
                alert("Successfully added policy");
                props.setter(false);
            }
        });
    };

    return (
        <div>
            <form onSubmit={submitHandler}>
                <div className={classes.control}>
                    <h1 className={classes.h1}>Enter policy information:</h1>
                    <label htmlFor="productID">Product ID: </label>
                    <input type="number" required id="productID" ref={productIDRef}/>
                </div>
                <div className={classes.control}>
                    <label htmlFor="min">Minimum amount: </label>
                    <input type="number" required id="min" ref={minRef}/>
                </div>
                <div>
                    <button className={classes.actions}>Add new policy</button>
                </div>
            </form>
        </div>
    );
};

export default AddAtLeast;
