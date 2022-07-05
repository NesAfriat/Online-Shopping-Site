import Card from "../../styling/Card";
import classes from "../../styling/item.module.css";

const Assignment = (props) => {
    return (
        <>
            <Card>
                <div className={classes.content}>
                <h3>Assignee name: {props.AssigneUserName}</h3>
                    <h4>Assigned by: {props.Assignor}</h4>
                    <h4>approvers:</h4>
                    <ul>
                        {props.approvers.map((name,i) =>{
                            return(
                                <li key={i}>
                                    <p>{name}</p>
                                </li>
                            )
                        })}
                    </ul>
                </div>
            </Card>
        </>
    );
}

export default Assignment;