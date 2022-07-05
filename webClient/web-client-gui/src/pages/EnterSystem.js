import classes from "../components/layouts/mainNavigation.module.css";
import EnterNavigation from "../components/layouts/EnterNavigation";
import LoginForm from "../components/Forms/LoginForm";
import MarketPage from "./Market";
import { Route, Switch } from "react-router-dom";
import RegisterForm from "../components/Forms/RegisterForm";

function EnterSystemPage(props) {
  console.log("entered system");
  return (
    <div>
      <h1 className={classes.h1}>Choose how to Identify:</h1>
      <div>
        <EnterNavigation />
        <Switch>
          <Route path="/login">
            <LoginForm />
          </Route>
          <Route path="/register">
            <RegisterForm />
          </Route>
          <Route path="/market">
            <MarketPage />
          </Route>
        </Switch>
      </div>
    </div>
  );
}

export default EnterSystemPage;
