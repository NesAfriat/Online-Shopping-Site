import {useRef} from "react";
import {postUpdateProductPrice} from "../../../../api";
import classes from "../../../../styling/addForm.module.css";

const UpdateProductPrice = (props) => {
    const prodIdRef = useRef()
    const priceRef = useRef()

    const submitHandler = async (e) => {
        e.preventDefault()

        if (prodIdRef.current.value === "" &&
            priceRef.current.value === "") {
            alert("please fill in all fields")
            return
        }

        const id = prodIdRef.current.value
        const price = priceRef.current.value
        if (price < 0) {
            alert("please enter a valid price amount")
            return
        }

        await postUpdateProductPrice(props.visitorId, props.shopId, id, price).then(res => {
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage)
            } else {
                alert("Successfully changed product price!");
                props.reload(props.shopId);
                props.setter(false);
            }
        })
    }
    return (
        <form onSubmit={submitHandler}>
            <div className={classes.control}>
                <h1 className={classes.h1}>Update product price:</h1>
            </div>
            <div className={classes.control}>
                <label htmlFor="">product Id: </label>
                <input type="text" required id="id" ref={prodIdRef} size="20"/>
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
                <button className={classes.actions}>set price</button>
            </div>
        </form>
    )
}
export default UpdateProductPrice