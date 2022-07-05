import {useRef} from 'react';
import classes from "../addForm.module.css";
import { useHistory } from "react-router-dom";
import { useState } from "react";

function PaymentForm(){
    const [validInput, setValid] = useState(true);
    const history = useHistory();
    const ccNumber= useRef();
    const expdate= useRef();
    const cvs= useRef();
    const country= useRef();
    const city= useRef();
    const street= useRef();
    function failInput()
    {   
        alert("illegal input, please try again");
        history.replace("/payment");
    }
    function Succes()
    {   
        alert("The action completed succesfuly");
        history.replace("/market")
    }
    function applyPayment(paymentData) 
    {
    ///axious take data and get it to the server
    //invalid input:
    //setValid(false)
    setValid(true)
    }
    function submitHandler(event)
    {
        event.preventDefault();
        const enteredccNumer = ccNumber.current.value;
        const enteredDate = expdate.current.value;
        const enteredCVS = cvs.current.value;
        const enteredCountry = country.current.value;
        const enteredCity = city.current.value;
        const enteredStreet = street.current.value;

        const paymentData={
             ccNumber: enteredccNumer,
             expdate: enteredDate,
             cvs: enteredCVS,
             country: enteredCountry,
             city: enteredCity,
             street: enteredStreet
        }
 
        applyPayment(paymentData);
        validInput? Succes() : failInput();
    }
     return <form onSubmit={submitHandler}>
        <div className={classes.control}> <h1 className={classes.h1}>Enter your payment and delivery information:</h1>
            <label htmlFor="Credit-card number">Credit-card number: </label>
            <input type="number" required id="Credit-card number" ref={ccNumber}/>
        </div>
        <div className={classes.control}> 
            <label htmlFor="expiration date">expiration date: </label>
            <input type="date" required id="expiration date" ref={expdate}/>
        </div>
        <div className={classes.control}> 
            <label htmlFor="cvs">cvs: </label>
            <input type="number" required id="cvs" ref={cvs}/>
        </div>
        <div className={classes.control}> 
            <label htmlFor="Country">Country: </label>
            <input type="text" required id="Country" ref={country}/>
        </div>
        <div className={classes.control}> 
            <label htmlFor="City">City: </label>
            <input type="text" required id="City" ref={city}/>
        </div>
        <div className={classes.control}> 
            <label htmlFor="Street">Street: </label>
            <input type="text" required id="Street" ref={street}/>
        </div>
        <div>
            <button className={classes.actions}>Sign up</button>
        </div>
    </form>
}

export default PaymentForm; 