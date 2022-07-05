import classes from "../components/addForm.module.css";
import { useHistory } from "react-router-dom";
function CartPage(props) {
  const history = useHistory();

  function cancelHandler() {
    history.replace("/market");
  }
  return (
    <div>
      <div>My Cart</div>
      <button className={classes.returnButton} onClick={cancelHandler}>
        Return
      </button>
    </div>
  );
}

export default CartPage;
