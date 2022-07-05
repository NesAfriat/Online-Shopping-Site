import classes from "../../styling/item.module.css";
import Card from "../../styling/Card";

const Owner = (props) => {
    return (
        <>
            <Card>
                <div className={classes.content}>
                    <h3>Owner username: {props.username} </h3>
                    {props.assignor ? <div><h3>Assignor: {props.assignor}</h3></div> : <div><h3>Founder</h3></div>}
                </div>
            </Card>
        </>
    );
};

export default Owner;
