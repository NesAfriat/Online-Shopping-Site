import {useRef} from "react";
import {postChangeProductDescription} from "../../../../api";
import classes from "../../../../styling/addForm.module.css";

const UpdateProductDesc = (props) => {
    const prodIdRef = useRef()
    const descRef = useRef()

    const submitHandler = async (e) => {
        e.preventDefault()

        if (prodIdRef.current.value === "" &&
            descRef.current.value === "") {
            alert("please fill in all fields")
            return
        }

        const id = prodIdRef.current.value
        const desc = descRef.current.value

        await postChangeProductDescription(props.visitorId, props.shopId, id, desc).then(res => {
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage)
            } else {
                alert("Successfully changed product description!");
                props.reload(props.shopId);
                props.setter(false);
            }
        })
    }
    return (
        <form onSubmit={submitHandler}>
            <div className={classes.control}>
                <h1 className={classes.h1}>Update product description:</h1>
            </div>
            <div className={classes.control}>
                <label htmlFor="">product Id: </label>
                <input type="text" required id="id" ref={prodIdRef} size="20"/>
            </div>
            <div className={classes.control}>
                <label htmlFor="">description: </label>
                <input
                    type="text"
                    required
                    id="desc"
                    ref={descRef}
                    size="20"
                />
            </div>
            <div>
                <button className={classes.actions}>set description</button>
            </div>
        </form>
    )
}
export default UpdateProductDesc
