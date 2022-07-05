import {useRef} from "react";
import {postIncrementBid} from "../../../../api";
import classes from "../../../../styling/addForm.module.css";

const IncrementBid = (props) => {
    const bidIdRef = useRef()
    const priceRef = useRef()

    const submitHandler = async (e) => {
        e.preventDefault()

        if (bidIdRef.current.value === "" &&
            priceRef.current.value === "") {
            alert("please fill in all fields")
            return
        }

        const id = bidIdRef.current.value
        const price = priceRef.current.value
        if (price < 0) {
            alert("please enter a valid price amount")
            return
        }

        await postIncrementBid(props.visitorId, props.shopId, id, price).then(res => {
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage)
            } else {
                alert("Successfully Incremented bid price!");
                props.setter(props.shopId);
            }
        })
    }
    return (
        <form onSubmit={submitHandler}>
            <div className={classes.control}>
                <h1 className={classes.h1}>Increment bid price:</h1>
            </div>
            <div className={classes.control}>
                <label htmlFor="">bid Id: </label>
                <input type="text" required id="id" ref={bidIdRef} size="20"/>
            </div>
            <div className={classes.control}>
                <label htmlFor="">price: </label>
                <input
                    type="text"
                    required
                    id="price"
                    ref={priceRef}
                    size="20"
                />
            </div>
            <div>
                <button className={classes.actions}>increment bid</button>
            </div>
        </form>
    )
}

export default IncrementBid