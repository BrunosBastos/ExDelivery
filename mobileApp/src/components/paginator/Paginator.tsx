import React from 'react';
import ArrowForwardIosIcon from '@material-ui/icons/ArrowForwardIos';
import ArrowBackIosIcon from '@material-ui/icons/ArrowBackIos';
import {
    Button,
    Grid,
  } from '@material-ui/core';

export default function Paginator(props) {

    return (
        <Grid container style={{flexGrow: 1}} spacing={3}>
            <Grid
              item
              xs={12}
              pt={50}
            >
                <Grid container >
                    <Grid
                    item
                    lg={1}
                    md={1}
                    xs={1}
                    >
                        {props.page !== 0 ? <ArrowBackIosIcon onClick={() => props.changePage(props.page - 1)} style={{cursor: 'pointer', color: 'rgb(0 6 255)'}} /> : <></> }
                    </Grid>
                    <Grid
                    item
                    lg={10}
                    md={10}
                    xs={10}
                    style={{textAlign:'center', maxWidth: "100%"}}
                    >
                        <Button style={{color: 'white', pointerEvents: 'none', background: 'rgb(0, 135, 255)'}}>
                            Page {props.page}
                        </Button>
                    </Grid>
                    <Grid
                    item
                    lg={1}
                    md={1}
                    xs={1}
                    >
                        {props.hasNext ? <ArrowForwardIosIcon onClick={() => props.changePage(props.page + 1)} style={{cursor: 'pointer', color: 'rgb(0 6 255)'}} /> : <></>}
                    </Grid>
                </Grid>
            </Grid>
        </Grid>
    )
}