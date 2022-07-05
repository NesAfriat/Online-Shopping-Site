import {useRef} from "react";
import {postAssignShopManager} from "../../../../api";
import classes from "../../../../styling/addForm.module.css";

const AddManager = (props) => {
    const memberToAssignRef = useRef();

    const submitHandler = async (e) => {
        e.preventDefault();
        const memberToAssign = memberToAssignRef.current.value;

        await postAssignShopManager(
            props.visitorId,
            memberToAssign,
            props.shopId
        ).then((res) => {
            console.log(res);
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage)
            } else {
                alert("Successfully assigned new manager!");
            }
        });
    };

    return (
        <div>
            <form onSubmit={submitHandler}>
                <div className={classes.control}>
                    <h1 className={classes.h1}>Enter member's username:</h1>
                    <label htmlFor="member">Username: </label>
                    <input type="text" required id="member" ref={memberToAssignRef}/>
                </div>
                <div>
                    <button className={classes.actions}>Assign new manager</button>
                </div>
            </form>
        </div>
    );
};

export default AddManager