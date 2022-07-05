import classes2 from "../../styling/list.module.css";
import Card from "../../styling/Card";
import Visitor from "../BasicComponent/Visitor";
import classes from "../../styling/item.module.css"

const ViewAllVisitors = (props) => {

    return (
        <>
            <h1 className={classes.content2}>Visitors in the server: {props.visitors.length}</h1>
            <h2 className={classes.content}>Visitors:</h2>
            <ul className={classes2.list}>
                {props.visitors.map((visitor, i) => {
                    return (
                        <li key={i}>
                            <Card>
                                <Visitor
                                    visitor={visitor}
                                    id={visitor.id}
                                    logged={visitor.loggedIn != null}
                                ></Visitor>
                            </Card>
                        </li>
                    );
                })}
            </ul>
            <></>
        </>
    );
};

export default ViewAllVisitors;