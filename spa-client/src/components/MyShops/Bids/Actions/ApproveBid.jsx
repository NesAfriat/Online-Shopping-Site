import classes from "../../../../styling/addForm.module.css";
import {useRef} from "react";
import {postApproveBid} from "../../../../api";

const ApproveBid = (props) => {
    const bidIdRef = useRef()

    const submitHandler = async (e) => {
        e.preventDefault()

        if (bidIdRef.current.value === "") {
            alert("please fill in all fields")
            return
        }

        const id = bidIdRef.current.value

        await postApproveBid(props.visitorId, props.shopId, id).then(res => {
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage)
            } else {
                alert("Successfully approved bid!");
                props.setter(props.shopId);
            }
        })
    }

    return (
        <form onSubmit={submitHandler}>
            <div className={classes.control}>
                <h1 className={classes.h1}>Approve bid:</h1>
            </div>
            <div className={classes.control}>
                <label htmlFor="">bid Id: </label>
                <input type="text" required id="id" ref={bidIdRef} size="20"/>
            </div>
            <div>
                <button className={classes.actions}>approve bid</button>
            </div>
        </form>
    )
}

export default ApproveBid