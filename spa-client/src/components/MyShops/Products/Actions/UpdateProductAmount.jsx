import {useRef} from "react";
import {postAddProductAmount, postReduceProductAmount} from "../../../../api";
import classes from "../../../../styling/addForm.module.css";

const UpdateProductAmount = (props) => {
    const prodIdRef = useRef()
    const amountRef = useRef()

    const submitHandler = async (e) => {
        e.preventDefault()
        let func;
        if (prodIdRef.current.value === "" &&
            amountRef.current.value === "") {
            alert("please fill in all fields")
            return
        }

        const id = prodIdRef.current.value
        const amount = amountRef.current.value
        if (amount < 0) {
            func = postReduceProductAmount
        } else {
            func = postAddProductAmount
        }
        await func(props.visitorId, props.shopId, id, amount).then(res => {
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage)
            } else {
                alert("Successfully changed product amount!");
                props.reload(props.shopId);
                props.setter(false);
            }
        })
    }
    return (
        <form onSubmit={submitHandler}>
            <div className={classes.control}>
                <h1 className={classes.h1}>Update product amount:</h1>
            </div>
            <div className={classes.control}>
                <label htmlFor="">product Id: </label>
                <input type="text" required id="id" ref={prodIdRef} size="20"/>
            </div>
            <div className={classes.control}>
                <label htmlFor="">amount: </label>
                <input
                    type="text"
                    required
                    id="amount"
                    ref={amountRef}
                    size="20"
                />
            </div>
            <div>
                <button className={classes.actions}>set amount</button>
            </div>
        </form>
    )
}
export default UpdateProductAmount
