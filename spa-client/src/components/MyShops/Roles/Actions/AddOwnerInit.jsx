import {useRef} from "react";
import {postInitAssignmentShopOwner} from "../../../../api";
import classes from "../../../../styling/addForm.module.css";

const AddOwnerInit = (props) => {
    const memberToAssignRef = useRef();

    const submitHandler = async (e) => {
        e.preventDefault();
        const memberToAssign = memberToAssignRef.current.value;

        await postInitAssignmentShopOwner(
            props.visitorId,
            memberToAssign,
            props.shopId
        ).then((res) => {
            console.log(res);
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage)
            } else {
                alert("Successfully init new owner assignment!");
            }
        });
    };

    return (
        <div>
            <h2 className={classes.content2}>Init assignemnt new shop owner:</h2>
            <form onSubmit={submitHandler}>
                <div className={classes.control}>
                    <h3 className={classes.h1}>Enter member's username:</h3>
                    <label htmlFor="member">Username: </label>
                    <input type="text" required id="member" ref={memberToAssignRef}/>
                </div>
                <div>
                    <button className={classes.actions}>Init Assignment</button>
                </div>
            </form>
        </div>
    );
};

export default AddOwnerInit