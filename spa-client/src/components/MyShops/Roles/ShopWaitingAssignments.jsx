import classes from "../../../styling/item.module.css";
import classes2 from "../../../styling/list.module.css";
import Card from "../../../styling/Card";
import Assignment from "../../BasicComponent/Assignment";

const ShopWaitingAssignments = (props) =>{
    return (
        <>
            <h2 className={classes.content2}>Waiting for approve:</h2>
            <ul className={classes2.list}>
                {props.assignments.map((assignment, i) => {
                    return (
                        <li key={i}>
                            <Card>
                                <Assignment
                                    visitorId={props.visitorId}
                                    shopId={props.shopId}
                                    id={assignment.id}
                                    Assignor={assignment.initiatorAssignor}
                                    AssigneUserName={assignment.toAssignMember}
                                    approvers={assignment.approvers}
                                />
                            </Card>
                        </li>
                    );
                })}
            </ul>
        </>
    );
}
export default ShopWaitingAssignments