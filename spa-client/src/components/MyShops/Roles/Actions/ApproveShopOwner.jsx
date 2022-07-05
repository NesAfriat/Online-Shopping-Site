import {useRef} from "react";
import {postApproveAssignShopOwner, postAssignShopOwner} from "../../../../api";
import classes from "../../../../styling/addForm.module.css";

const ApproveShopOwner = (props) =>{
    const memberToAssignRef = useRef();

    const submitHandler = async (e) => {
        e.preventDefault();
        const memberToAssign = memberToAssignRef.current.value;

        await postApproveAssignShopOwner(
            props.visitorId,
            memberToAssign,
            props.shopId
        ).then((res) => {
            console.log(res);
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage)
            } else {
                alert("Successfully approved owner!");
            }
        });
    };

    return (
        <div>
            <h2 className={classes.content2}>Approve shop owner assignment:</h2>
            <form onSubmit={submitHandler}>
                <div className={classes.control}>
                    <h3 className={classes.h1}>Enter member's username:</h3>
                    <label htmlFor="member">Username: </label>
                    <input type="text" required id="member" ref={memberToAssignRef}/>
                </div>
                <div>
                    <button className={classes.actions}>Approve owner</button>
                </div>
            </form>
        </div>
    );
}
export default ApproveShopOwner