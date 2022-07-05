import classes from "../../styling/addForm.module.css";
import {useRef} from "react";
import {getDailyData} from "../../api";
import {useState} from "react";

const ViewDailyData = (props) => {
    const [showData, setShowData] = useState(false)
    const [data, setData] = useState([])
    const startDateRef = useRef();
    const endDateRef = useRef();

    const submitHandler = async (e) => {
        e.preventDefault();

        const startDate = startDateRef.current.value;
        let dateArr = startDate.split('-')
        const startDateFomated = dateArr[2]+"/"+dateArr[1]+"/"+dateArr[0]
        const endDate = endDateRef.current.value;
        dateArr = startDate.split('-')
        const endDateFormated = dateArr[2]+"/"+dateArr[1]+"/"+dateArr[0]

        console.log(`start date: ${startDateFomated}`)
        console.log(`end date: ${endDateFormated}`)
        await getDailyData(
            props.visitorId,
            startDateFomated,
            endDateFormated
        ).then((res) => {
            console.log(res);
            if (res.errorOccurred) {
                alert(res.errorMessage);
                console.log(res.errorMessage)
            } else {
                setShowData(true);
                setData([...res.value])
            }
        });
    };


    return (
        <>
            <form onSubmit={submitHandler}>
                <div className={classes.control}>
                    <h1 className={classes.h1}>
                        Enter dates for daily information:
                    </h1>
                    <div className={classes.control}>
                        <label htmlFor="start date">start date: </label>
                        <input type="date" required id="start date" ref={startDateRef}/>
                    </div>
                    <div className={classes.control}>
                        <label htmlFor="end date">end date: </label>
                        <input type="date" required id="end date" ref={endDateRef}/>
                    </div>
                    <div>
                        <button className={classes.actions}>get data</button>
                    </div>
                </div>
            </form>
            {showData && (
                <>
                    <ul>
                    {data.map((dailyData,i) => {
                        return(
                            <li key={i}>
                                <h3>data for date: {dailyData.date}</h3>
                                <h3>visitors: {dailyData.visitorCount}</h3>
                                <h3>members without role: {dailyData.roleLessMembersCount}</h3>
                                <h3>shop managers: {dailyData.shopManagersCount}</h3>
                                <h3>shop owners: {dailyData.shopOwnerCount}</h3>
                                <h3>admins: {dailyData.AdminCount}</h3>
                            </li>
                        )
                    })}
                    </ul>
                </>
            )}
        </>
    );
}

export default ViewDailyData