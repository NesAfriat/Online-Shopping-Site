import classes from "./card.module.css";
function Card(props) {
    return <div className={classes.Card}>{props.children}</div>;
}

export default Card;
