import React from "react";
import ReactSelect from "react-select";
import "../../css/Select.css";
import en from "../localize/en";
import customStyles from "./CustomStyles";

const Select = props => {
  const {
    label,
    name,
    isRequire = false,
    options,
    value,
    placeholder,
    disabled = false,
    isMultiple = false,
    allowSelectAll = false
  } = props;

  const allOption = { label: en.IA_ALL, value: "*" };

  const onChange = selected => {
    if (isMultiple) {
      if (
        allowSelectAll &&
        selected !== null &&
        selected.length > 0 &&
        selected[selected.length - 1].value === allOption.value
      ) {
        props.onChange(options);
      } else {
        props.onChange(selected);
      }
    } else {
      props.onChange({
        key: name,
        value: selected.value,
        label: selected.label,
        detailedInformation: selected.detailedInformation
      });
    }
  };

  return (
    <div className="custom-select-wrapper">
      {label && (
        <label className="select-label">
          {label}
          {isRequire ? <span className="select-required">*</span> : null}
        </label>
      )}
      <ReactSelect
        styles={customStyles}
        isClearable={false}
        options={allowSelectAll ? [allOption, ...options] : options}
        onChange={onChange}
        defaultValue={value}
        placeholder={placeholder}
        isOptionDisabled={option => option.disabled}
        isDisabled={disabled}
        isMulti={isMultiple}
      />
    </div>
  );
};

/**
 const options = [
  { label: 'one', value: 1, disabled: true },
  { label: 'two', value: 2 },
  { label: 'three', value: 3 },
  { label: 'four', value: 4 },
  { label: 'five', value: 5, disabled: true }
];
<Select
  onChange={(option)=>console.log(option)}
  options={options}
  value={selected}
  placeholder={'Select item'}
  />
 */

export default Select;
