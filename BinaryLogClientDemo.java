
        BinaryLogClient client = new BinaryLogClient(hostName, port, userName, password);

        client.registerEventListener(event -> {
            EventData data = event.getData();
            EventHeaderV4 headerV4 = event.getHeader();
            String database = null;
            Serializable[] series;
            switch (headerV4.getEventType()) {
                case EXT_WRITE_ROWS:
                    WriteRowsEventData queryEventData = (WriteRowsEventData) data;
                    series = queryEventData.getRows().get(0);
                    database = table.get(queryEventData.getTableId());
                    if (database != null) {
                        System.out.print("Insert table " + database + ": ");
                        printArr(series);
                    }
                    break;
                case EXT_UPDATE_ROWS:
                    UpdateRowsEventData updateRowsEventData = (UpdateRowsEventData) data;
                    series = updateRowsEventData.getRows().get(0).getValue();
                    database = table.get(updateRowsEventData.getTableId());
                    if (database != null) {
                        System.out.print("Update table " + database + ": ");
                        printArr(series);
                    }
                    break;
                case EXT_DELETE_ROWS:
                    DeleteRowsEventData deleteRowsEventData = (DeleteRowsEventData) data;
                    series = deleteRowsEventData.getRows().get(0);
                    database = table.get(deleteRowsEventData.getTableId());
                    if (database != null) {
                        System.out.print("Delete table " + database + ": ");
                        printArr(series);
                    }
                    break;
                case TABLE_MAP:
                    TableMapEventData tableMapEventData = (TableMapEventData) data;
                    if (table.get(tableMapEventData.getTableId()) == null)
                        table.put(tableMapEventData.getTableId(),
                                tableMapEventData.getDatabase() + "." + tableMapEventData.getTable());
                    break;
                case QUERY:
//                    QueryEventData eventData = (QueryEventData) data;
//                    System.out.println(eventData.getSql());
                    break;
                default:
                    break;
            }
        });
        try {
            client.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
