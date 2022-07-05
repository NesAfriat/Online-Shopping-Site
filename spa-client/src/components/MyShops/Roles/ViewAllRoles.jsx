import classes2 from "../../../styling/list.module.css";
import Card from "../../../styling/Card";
import classes from "../../../styling/item.module.css";
import Role from "../../BasicComponent/Role";

const ViewAllRoles = (props) => {
    return (
        <>
            <h1 className={classes.content2}>Roles in the Shop:</h1>
            <ul className={classes2.list}>
                {props.shopRoles.map((shopRole, i) => {
                    return (
                        <li key={i}>
                            <Card>
                                <Role
                                    visitorId={props.visitorId}
                                    shopId={props.shopId}
                                    member={shopRole.memberUserName}
                                    assignor={shopRole.assignorUsername}
                                    founder={props.founder}
                                    reload={props.reload}
                                ></Role>
                            </Card>
                        </li>
                    );
                })}
            </ul>
            <></>
        </>
    );
};

export default ViewAllRoles