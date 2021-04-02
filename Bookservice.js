import axiosClient from '../../service/axiosClient';

const getBooks = (params) => {
  return axiosClient.get('/api/book', { params });
};

const createBook = (data) => {
  return axiosClient.post('/api/book', data);
};

const updateBook = (data) => {
  return axiosClient.put('/api/book/' + data.id, data);
};

const deleteBook = (id) => {
  return axiosClient.delete('/api/book/' + id);
};

const BookService = {
  getBooks,
  createBook,
  updateBook,
  deleteBook
};

export default BookService;

=== axios: bỏ create axios
axios.interceptors.request.use(
  function (config) {
    const newConfig = {
      ...config,
      paramsSerializer: (params) =>
        queryString.stringify(cleanParam(params), { arrayFormat: 'repeat' }),
      baseURL: process.env.REACT_APP_HOST_API,
      withCredentials: true,
      headers: {
        ...config.headers,
        Pragma: 'no-cache'
      }
    };

    // Add auth token
    if(localStorage.getItem(ACCESS_TOKEN)) {
      newConfig.headers.Authorization = `Bearer ${localStorage.getItem(ACCESS_TOKEN)}`;
    }

    return newConfig;
  },
  (error) => {
    console.log(error);
    return Promise.reject(error);
  }
);

===
  import React, { PureComponent } from 'react';
import InputText from '../../components/Input/InputText';
import Select from '../../components/Input/Select';
import NormalModal from '../../components/Modal/NormalModal';
import { ACTION_ADD, ACTION_EDIT } from '../../constants/Constants';
import Toast from '../../components/Toast/Toast';
import BookService from './BookService';

class BookUpsertModal extends PureComponent {
  constructor(props) {
    super(props);
    const { id, title, author, category, price } = props.selectedRow;
    this.state = {
      id,
      title,
      author,
      category,
      price,
      invalid: {
        title: false
      }
    };
  }

  handleOnChange = (obj) => {
    const copyOfInvalid = { ...this.state.invalid, [obj.name]: obj.invalid };
    this.setState({
      [obj.name]: obj.value,
      invalid: copyOfInvalid
    });
  };

  handleOnChangeCategory = (obj) => {
    this.setState({
      category: { label: obj.label, value: obj.value }
    });
  };

  onSave = () => {
    const { id, title, author, category, price, invalid } = this.state;
    if (!title || !author || !category || !price) {
      Toast.error('Please fill all required fields');
      return;
    }
    if(invalid.price) {
      Toast.error('Price is not a valid number!');
      return;
    }
    const data = {
      id,
      title,
      author,
      categoryId: category.value,
      price
    };
    const { action } = this.props;
    if (action === ACTION_ADD) {
      BookService.createBook(data)
        .then((res) => {
          Toast.success(res.message);
          this.props.onSave();
        })
        .catch((err) => {
          console.log(err);
          Toast.error(err);
        });
    } else {
      BookService.updateBook(data)
        .then((res) => {
          Toast.success(res.message);
          this.props.onSave();
        })
        .catch((err) => {
          console.log(err);
          Toast.error(err);
        });
    }
  };

  render() {
    const { showUpsertModal, onCloseUpsertModal, categoryOptions } = this.props;
    const { title, author, category, price } = this.state;

    return (
      <NormalModal
        show={showUpsertModal}
        modalTitle="Add new book"
        saveButtonText="Save"
        cancelButtonText="Cancel"
        onSave={this.onSave}
        onClose={onCloseUpsertModal}
        onCancel={onCloseUpsertModal}
      >
        <InputText
          name="title"
          label="Title"
          defaultValue={title}
          isRequire={true}
          onChange={this.handleOnChange}
        />
        <InputText
          name="author"
          label="Author"
          defaultValue={author}
          isRequire={true}
          onChange={this.handleOnChange}
        />
        <Select
          name="category"
          label="Category"
          defaultValue={category}
          options={categoryOptions}
          isRequire={true}
          onChange={this.handleOnChangeCategory}
        />
        <InputText
          name="price"
          label="Price"
          defaultValue={price}
          regex="[0-9]+$"
          isRequire={true}
          onChange={this.handleOnChange}
        />
      </NormalModal>
    );
  }
}

export default BookUpsertModal;
