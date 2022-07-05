import {useRef} from "react";
import classes from "../../../../styling/addForm.module.css";
import {postAddAtMostFromProductPolicy} from "../../../../api";

const AddAtMost = (props) => {
    const productIDRef = useRef();
    const maxRef = useRef();

    const submitHandler = async (e) => {
        e.preventDefault();
        const enteredProductID = productIDRef.current.value;
        const enteredMax = maxRef.current.value;

        await postAddAtMostFromProductPolicy(
            props.visitorId,
            props.shopId,
            enteredProductID,
            enteredMax
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
                    <label htmlFor="max">Maximum amount: </label>
                    <input type="number" required id="max" ref={maxRef}/>
                </div>
                <div>
                    <button className={classes.actions}>Add new policy</button>
                </div>
            </form>
        </div>
    );
};

export default AddAtMost;
