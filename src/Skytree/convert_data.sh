generate-header.sh \
-file input/Axion_Ford_Data_Train1 \
-header_out output/Axion_Ford_Data_Train1.header \
-delimiter COMMA \
-label_index 6 \
-use_column_names \
-ignore_inconsistent_rows \
-missing_value ? \
-id_index 1


convert-data.sh \
-file input/Axion_Ford_Data_Train1 \
-header_in output/Axion_Ford_Data_Train1.header \
-ignore_lines 1 \
-label_index 6 \
-id_index 1 \
-ignore_missing \
-rare_words_pct 10.0 \
-stop_words_pct 15.0 \
-ignore_new_words \
-ignore_out_of_range \
-data_out output/Axion_Ford_Data_Train1.st \
-labels_out output/Axion_Ford_Data_Train1.labels