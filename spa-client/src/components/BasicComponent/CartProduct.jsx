import classes from "../../styling/item.module.css";
import Card from "../../styling/Card";
import {postremoveProductFromShoppingCart} from "../../api";

const CartProduct = (props) => {

    const removeProduct = async (e) => {
        e.preventDefault();

        await postremoveProductFromShoppingCart(
            props.visitorId,
            props.shopId,
            props.id
        ).then((res) => {
            console.log(res);
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage);
            } else {
                alert("Successfully removed from cart!");
            }
            props.reloadCart();
        });
    };

    return (
        <>
            <Card>
                <div className={classes.content}>
                    <h3>Product name: {props.name}</h3>
                    <h3>Product ID: {props.id}</h3>
                    <h3>Price: {props.price}</h3>
                    <h3>Amount: {props.qunatity}</h3>
                    <h4>Description: {props.description}</h4>
                    <div>
                        <button onClick={removeProduct}>Remove</button>
                        {" "}
                    </div>
                </div>
            </Card>
        </>
    );
};

export default CartProduct;
