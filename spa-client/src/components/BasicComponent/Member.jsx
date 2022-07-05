import classes from "../../styling/item.module.css";
import Card from "../../styling/Card";
import {postCancelMemberShip} from "../../api";

const Member = (props) => {

    const removeMember = async (member) => {
        console.log(`clicked on removeMember`);
        await postCancelMemberShip(props.visitorId, member).then((res) => {
            console.log("removing member");
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage);
                return;
            } else {
                alert("Membership cancelled succesfully");
                props.reload();
            }
        });
    };

    return (
        <>
            <Card>
                <div className={classes.content}>
                    <h3>Member : {props.username} </h3>
                    <button
                        onClick={() => removeMember(props.username)}
                    >
                        Cancel membership
                    </button>
                </div>
            </Card>
        </>
    );
};

export default Member;
