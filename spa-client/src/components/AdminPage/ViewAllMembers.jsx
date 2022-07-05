import classes2 from "../../styling/list.module.css";
import Card from "../../styling/Card";
import Member from "../BasicComponent/Member";
import classes from "../../styling/item.module.css"

const ViewAllMembers = (props) => {
    console.log("viewing members");
    return (
        <>
            <h1 className={classes.content2}>Members in the server: {props.members.length}</h1>
            <h2 className={classes.content}>Members:</h2>
            <ul className={classes2.list}>
                {props.members.map((member, i) => {
                    return (
                        <li key={i}>
                            <Card>
                                <Member
                                    username={member.username}
                                    visitorId={props.visitorId}
                                    reload={props.reload}
                                ></Member>
                            </Card>
                        </li>
                    );
                })}
            </ul>
            <></>
        </>
    );
};

export default ViewAllMembers;