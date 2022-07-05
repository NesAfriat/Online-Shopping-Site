import classes from "../../styling/item.module.css";
import Card from "../../styling/Card";
import {useRef, useState} from "react";
import {postAddProductToShoppingCart, postBidProduct} from "../../api";

const GeneralProduct = (props) => {
    const [addToCart, setAddToCart] = useState(false);
    const [addToBid, setAddToBid] = useState(false);
    const quantityRef = useRef();

    const quantityRefBid = useRef();
    const priceRefBid = useRef();

    const handleAddToCart = () => {
        setAddToCart(!addToCart);
    }

    const handleAddToBid = () => {
        setAddToBid(!addToBid);
    }

    const submitHandler = async (e) => {
        e.preventDefault();
        if (quantityRef.current.value < 1) {
            alert("Please fill legal amount");
            return;
        }
        const quantity = quantityRef.current.value;

        await postAddProductToShoppingCart(props.visitorId, props.shopId, props.id, quantity).then((res) => {
            console.log(res);
            if (res.errorOccurred) {
                alert(res.errorMessage);
            } else {
                alert("Successfully added to cart!");
                setAddToCart(false);
            }
        });

        quantityRef.current.value = "";
    };

    const submitBid = async (e) => {
        e.preventDefault();
        const quantity = quantityRefBid.current.value;
        const price = Number(priceRefBid.current.value);

        if (quantity < 1) {
            alert("Please fill legal amount");
            return;
        }
        if (price < 0) {
            alert("Cannot bid for negative price")
            return;
        }

        await postBidProduct(props.visitorId, props.shopId, props.id, quantity, price).then((res) => {
            console.log(res);
            if (res.errorOccurred) {
                alert(res.errorMessage);
            } else {
                alert("Successfully added bid!");
                setAddToBid(false);
            }
        });

        quantityRefBid.current.value = "";
        priceRefBid.current.value = "";
    };

    return (
        <>
            <Card>
                <div className={classes.content}>
                    <h3>Product ID: {props.id}</h3>
                    <h4>Product name: {props.name}</h4>
                    <h3>Category: {props.category}</h3>
                    <h3>Price: {props.price}</h3>
                    <h3>Amount: {props.quantity}</h3>
                    <p>Description: {props.description}</p>
                    <div>
                        <button onClick={handleAddToCart}>Add to cart</button>
                    </div>
                    {addToCart && <form onSubmit={submitHandler}>
                        <div className={classes.control}>
                            <label htmlFor="quantity">Amount: </label>
                            <input type="number" required id="quantity" ref={quantityRef}/>
                        </div>
                        <div>
                            <button className={classes.actions}>Add</button>
                        </div>
                    </form>}
                    {props.member !== "" &&
                        <div>
                            <div>
                                <button onClick={handleAddToBid}>Place a bid</button>
                            </div>
                            {addToBid && <form onSubmit={submitBid}>
                                <div className={classes.control}>
                                    <label htmlFor="quantity">Amount: </label>
                                    <input type="number" required id="quantity" ref={quantityRefBid}/>
                                </div>
                                <div className={classes.control}>
                                    <label htmlFor="price">bid price: </label>
                                    <input type="text" required id="quantity" ref={priceRefBid}/>
                                </div>
                                <div>
                                    <button className={classes.actions}>Add</button>
                                </div>
                            </form>}
                        </div>}
                </div>
            </Card>
        </>
    )
}

export default GeneralProduct;