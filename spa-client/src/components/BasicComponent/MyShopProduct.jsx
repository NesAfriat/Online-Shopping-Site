import classes from "../../styling/item.module.css";
import Card from "../../styling/Card";
import {postRemoveProduct} from "../../api";

const MyShopProduct = (props) => {


    const removeProduct = async (e) => {
        e.preventDefault();

        await postRemoveProduct(props.visitorId, props.shopId, props.id).then((res) => {
            console.log(res);
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage);
            } else {
                alert("Successfully removed from shop!");
            }
            props.setter(props.shopId);
        });

    };

    return (
        <>
            <Card>
                <div className={classes.content}>
                    <h3>Product ID: {props.id}</h3>
                    <h4>Product name: {props.name}</h4>
                    <h3>Category: {props.category}</h3>
                    <h3>Price: {props.price}</h3>
                    <h3>Amount: {props.qunatity}</h3>
                    <p>Description: {props.description}</p>
                    <button onClick={removeProduct}>Remove</button>
                </div>
            </Card>
        </>)
};

export default MyShopProduct;