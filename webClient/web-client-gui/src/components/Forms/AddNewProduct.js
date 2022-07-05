import { useRef } from "react";
import { useHistory } from "react-router-dom";
import { useState } from "react";
import { postAddProduct } from "../../api";
import classes from "../addForm.module.css";
import visitorId from "../VisitorId";

function AddNewProduct(props) {
  const [validInput, setValid] = useState(true);
  const history = useHistory();
  const nameRef = useRef();
  const quantityRef = useRef();
  const priceRef = useRef();
  const descriptionRef = useRef();
  const categoryRef = useRef();
  function failInput(res) {
    setValid(false);
    alert(res.ErrorMessage);
    history.replace(`/all-shops/${props.shopId}/add-new-product`);
  }
  function Succes() {
    setValid(true);
    alert("The action completed succesfuly");
    history.replace(`/all-shops/${props.shopId}`);
  }

  const AddProduct = async (productData) => {
    const result = await postAddProduct(
      visitorId.id,
      props.shopId,
      productData.name,
      productData.quantity,
      productData.price,
      productData.description,
      productData.category
    ).then((res) => {
      if (res.ErrorMessage != null) {
        setValid(false);
      } else {
        setValid(true);
      }
      console.log(res);
      return res;
    });
    return result;
  };
  function cancelHandler() {
    console.log("the shop id:");
    console.log(props.shopId);
    history.replace(`/all-shops/${props.shopId}`);
  }
  function submitHandler(event) {
    event.preventDefault();
    const enteredName = nameRef.current.value;
    const enteredQuantity = quantityRef.current.value;
    const enteredPrice = priceRef.current.value;
    const enteredDescription = descriptionRef.current.value;
    const enteredCategory = categoryRef.current.value;

    const productData = {
      name: enteredName,
      quantity: enteredQuantity,
      price: enteredPrice,
      description: enteredDescription,
      category: enteredCategory,
    };

    const res = AddProduct(productData);
    validInput ? Succes() : failInput(res);
  }

  console.log("adding a product");
  return (
    <div className="wrapper">
      <form onSubmit={submitHandler}>
        Fill in the product's details:
        <fieldset>
          <label>
            <p>Product's name:</p>
            <input type="text" required id="productname" ref={nameRef} />
          </label>
          <label>
            <p>Amount:</p>
            <input type="number" required id="quantity" ref={quantityRef} />
          </label>
          <label>
            <p>Price:</p>
            <input type="number" required id="price" ref={priceRef} />
          </label>
          <label>
            <p>Category:</p>
            <input type="text" required id="category" ref={categoryRef} />
          </label>
          <div className={classes.control}>
            <label>
              <p>Description:</p>
              <text
                required
                type="text"
                id="category"
                size="20"
                ref={descriptionRef}
              />
            </label>
          </div>
        </fieldset>
        <button type="submit">Submit</button>
      </form>
      <button className={classes.returnButton} onClick={cancelHandler}>
      Return
    </button>
    </div>
  );
}

export default AddNewProduct;
