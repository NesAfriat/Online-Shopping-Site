import classes from "../components/item.module.css";
import { Route, Switch } from "react-router-dom";
import MarketNavigation from "../components/layouts/MarketNavigation";
import SearchProduct from "../components/Forms/SearchProduct";
import AllShops from "./AllShops";
import OpenShopForm from "../components/Forms/OpenShopForm";
import CartPage from "./CartPage";
import RegisterForm from "../components/Forms/RegisterForm";
import LoginForm from "../components/Forms/LoginForm";
import App from "../App";
import visitorId from "../components/VisitorId";

function MarketPage(props) {
  function handleLeave() {
    //leave system axios
    console.log("leaving system");
    visitorId.id = -1;
  }
  console.log("entered market");
  return (
    <div>
      <MarketNavigation leaveSystem={handleLeave} />
      <Switch>
        <Route path="/my-cart">
          <CartPage />
        </Route>
        <Route path="/search-product">
          <SearchProduct />
        </Route>
        <Route path="/all-shops">
          <AllShops />
        </Route>
        <Route path="/open-shop">
          <OpenShopForm />
        </Route>
        <Route path="/register">
          <RegisterForm />
        </Route>
        <Route path="/login">
          <LoginForm />
        </Route>
        <Route path="/" exact>
          <App />
        </Route>
      </Switch>
      <div className={classes.image}>
        <img
          src="https://industrialstrengthsem.com/wp-content/uploads/2019/07/marketing-online-strategy-with-drawings_1134-76-1.jpg"
          alt="Online marketing"
        />
      </div>
    </div>
  );
}
export default MarketPage;
