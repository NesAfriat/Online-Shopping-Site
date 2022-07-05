import classes from "../../styling/item.module.css";
import Card from "../../styling/Card";
import ViewAllProducts from "../ViewAllProducts";

const Shop = (props) => {
    return (
        <>
            <Card>
                <div className={classes.content}>
                    <h1>{props.name}'s shop</h1>
                    <h3>founded by {props.founder}</h3>
                    <p>located at: {props.location}</p>
                    <p>general description: {props.description}</p>
                    {props.open ? <div>
                        <p>Open</p></div> : <><p>Close</p></>}
                    {props.products !== undefined && props.products.length > 0 &&
                        <ViewAllProducts products={[...props.products]} shopId={props.id} visitorId={props.visitorId}/>}
                </div>
            </Card>
        </>
    )
}

export default Shop;