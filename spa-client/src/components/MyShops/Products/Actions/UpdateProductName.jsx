import classes from "../../../../styling/addForm.module.css";
import {useRef} from "react";
import {postChangeProductName} from "../../../../api";

const UpdateProductName = (props) => {
    const prodIdRef = useRef()
    const nameRef = useRef()

    const submitHandler = async (e) => {
        e.preventDefault()

        if (prodIdRef.current.value === "" &&
            nameRef.current.value === "") {
            alert("please fill in all fields")
            return
        }

        const id = prodIdRef.current.value
        const name = nameRef.current.value

        await postChangeProductName(props.visitorId, props.shopId, id, name).then(res => {
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage)
            } else {
                alert("Successfully changed product name!");
                props.reload(props.shopId);
                props.setter(false)
            }
        })
    }
    return (
        <form onSubmit={submitHandler}>
            <div className={classes.control}>
                <h1 className={classes.h1}>Update product name:</h1>
            </div>
            <div className={classes.control}>
                <label htmlFor="">product Id: </label>
                <input type="text" required id="id" ref={prodIdRef} size="20"/>
            </div>
            <div className={classes.control}>
                <label htmlFor="">name: </label>
                <input
                    type="text"
                    required
                    id="name"
                    ref={nameRef}
                    size="20"
                />
            </div>
            <div>
                <button className={classes.actions}>set name</button>
            </div>
        </form>
    )
}
export default UpdateProductName