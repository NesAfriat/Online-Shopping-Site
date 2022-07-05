import classes2 from "../../styling/list.module.css";
import Card from "../../styling/Card";
import Owner from "../BasicComponent/Owner";
import classes from "../../styling/item.module.css"

const ViewAllOwners = (props) => {
    return (
        <>
            <h1 className={classes.content2}>Owners in the server: {props.owners.length}</h1>
            <h2 className={classes.content}>Owners:</h2>
            <ul className={classes2.list}>
                {props.owners.map((owner, i) => {
                    return (
                        <li key={i}>
                            <Card>
                                <Owner
                                    username={owner.memberUserName}
                                    assignor={owner.assignorUsername}
                                ></Owner>
                            </Card>
                        </li>
                    );
                })}
            </ul>
            <></>
        </>
    );
};

export default ViewAllOwners;