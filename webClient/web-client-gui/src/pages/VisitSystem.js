import visitorId from "../components/VisitorId";
import EnterSystemPage from "./EnterSystem";
import MainNavigation from "../components/layouts/MainNavigation";
import { Route, Switch } from "react-router-dom";
import { useState } from "react";
import LoginPage from "../components/Forms/LoginForm";
import MarketPage from "./Market";
import RegisterForm from "../components/Forms/RegisterForm";
import SearchProduct from "../components/Forms/SearchProduct";
import AllShops from "./AllShops";
import OpenShopForm from "../components/Forms/OpenShopForm";
import CartPage from "./CartPage";
import { postVisit } from "../api";

function VisitSystemPage() {
  const [visitorConnected, setVisitorConnected] = useState(false);

  const clickHandler = async () => {
    setVisitorConnected(true);
    console.log("clicked on visit");
    //visitSystem and keep id, axious
    const id = await postVisit().then((res) => {
      visitorId.id = res.value.id;
      console.log(visitorId.id);
    });
    return id;
  };

  return (
    <div>
      <MainNavigation click={clickHandler} connected={!visitorConnected} />
      <Switch>
        <Route path="/enter-system">
          <EnterSystemPage />
        </Route>
        <Route path="/login">
          <LoginPage />
        </Route>
        <Route path="/register">
          <RegisterForm />
        </Route>
        <Route path="/market">
          <MarketPage />
        </Route>
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
      </Switch>
    </div>
  );
}

export default VisitSystemPage;
