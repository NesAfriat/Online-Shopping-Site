import ShopList from "../components/shops/ShopList";
import { useHistory } from "react-router-dom";
import classes from "../components/addForm.module.css";

function AllShops() {
  const history = useHistory();

  function cancelHandler() {
    history.replace("/market");
  }

  return (
    <section>
      <ShopList />
      <div>
        <button className={classes.returnButton} onClick={cancelHandler}>
          Return
        </button>
      </div>
    </section>
  );
}

export default AllShops;
